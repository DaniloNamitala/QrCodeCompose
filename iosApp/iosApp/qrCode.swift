import UIKit
import AVFoundation
import MLKitBarcodeScanning
import MLKitVision
import ComposeApp

class IOSNativeViewFactory: NativeViewFactory {
    static var shared = IOSNativeViewFactory()

    func createQRCodeScannerView(
        onCodeRead: @escaping (KotlinByteArray?) -> Void
    ) -> UIViewController {
        return QRCodeScannerViewController(onCodeRead: onCodeRead)
    }
}

class QRCodeScannerViewController: UIViewController, AVCaptureVideoDataOutputSampleBufferDelegate {
    var captureSession: AVCaptureSession!
    var previewLayer: AVCaptureVideoPreviewLayer!
    var onCodeRead: (KotlinByteArray?) -> Void
    var barcodeScanner: BarcodeScanner!

    init(onCodeRead: @escaping (KotlinByteArray?) -> Void) {
        self.onCodeRead = onCodeRead
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .black

        // Configurar MLKit para ler QR Codes
        let options = BarcodeScannerOptions(formats: [.qrCode, .dataMatrix, .PDF417])
        barcodeScanner = BarcodeScanner.barcodeScanner(options: options)

        captureSession = AVCaptureSession()

        guard let device = AVCaptureDevice.default(for: .video),
              let input = try? AVCaptureDeviceInput(device: device)
        else {
            return
        }

        captureSession.addInput(input)

        let videoOutput = AVCaptureVideoDataOutput()
        videoOutput.setSampleBufferDelegate(self, queue: DispatchQueue(label: "qrQueue"))
        captureSession.addOutput(videoOutput)

        previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.frame = view.layer.bounds
        previewLayer.videoGravity = .resizeAspectFill
        view.layer.addSublayer(previewLayer)

        captureSession.startRunning()
    }

    func captureOutput(_ output: AVCaptureOutput, didOutput sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {
        let visionImage = VisionImage(buffer: sampleBuffer)
        visionImage.orientation = .right

        barcodeScanner.process(visionImage) { barcodes, error in
            if let error = error {
                print("Erro ao processar código: \(error)")
                return
            }

            guard let barcode = barcodes?.first,
                  let data = barcode.rawData
            else { return }

            self.captureSession.stopRunning()

            let kotlinBytes = KotlinByteArray(size: Int32(data.count))
            for (i, byte) in data.enumerated() {
                kotlinBytes.set(index: Int32(i), value: Int8(bitPattern: byte))
            }

            DispatchQueue.main.async {
                self.onCodeRead(kotlinBytes)
            }
        }
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        if captureSession.isRunning {
            captureSession.stopRunning()
        }
    }
}
